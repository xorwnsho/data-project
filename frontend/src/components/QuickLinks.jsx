import { Link, useLocation } from "react-router-dom";

const ICONS = {
	chat: (
		<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.6">
			<path d="M4 5h16v11H8l-4 4V5z" strokeLinejoin="round" />
			<path d="M8 10h8M8 13h5" strokeLinecap="round" />
		</svg>
	),
	explore: (
		<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.6">
			<circle cx="11" cy="11" r="7" />
			<path d="M20 20l-4.3-4.3" strokeLinecap="round" />
		</svg>
	),
	compare: (
		<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.6">
			<path d="M6 4v16M18 4v16" strokeLinecap="round" />
			<path d="M3 8l3-4 3 4M15 16l3 4 3-4" strokeLinecap="round" strokeLinejoin="round" />
		</svg>
	),
	star: (
		<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.6">
			<path d="M12 3l2.6 5.6 6 .7-4.4 4.2 1.1 6-5.3-3-5.3 3 1.1-6L3.4 9.3l6-.7L12 3z" strokeLinejoin="round" />
		</svg>
	),
};

const ITEMS = [
	{ to: "/", label: "AI 창업 상담", sub: "자연어 질의 → AI 리포트", icon: "chat", tone: "teal" },
	{ to: "/explore", label: "상권 탐색기", sub: "지도 + 통계 즉시 조회", icon: "explore", tone: "yellow" },
	{ to: "/compare", label: "지역·업종 비교", sub: "두 지역 나란히 비교", icon: "compare", tone: "blue" },
	{ to: "/favorites", label: "관심 지역", sub: "저장하고 다시 확인", icon: "star", tone: "coral" },
];

export default function QuickLinks() {
	const { pathname } = useLocation();

	return (
		<div className="quick-grid">
			{ITEMS.map((item) => {
				const isCurrent = item.to === pathname;
				const href = isCurrent && pathname === "/" ? "#query-form" : item.to;
				const Tag = isCurrent && pathname === "/" ? "a" : Link;
				const linkProp = Tag === "a" ? { href } : { to: href };

				return (
					<Tag
						key={item.to}
						{...linkProp}
						className={`quick-block ${item.tone}${isCurrent ? " current" : ""}`}
					>
						<span className="quick-icon">{ICONS[item.icon]}</span>
						<span className="quick-text">
							<strong>{item.label}</strong>
							<span>{item.sub}</span>
						</span>
					</Tag>
				);
			})}
		</div>
	);
}
